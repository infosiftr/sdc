import * as _ from "lodash";
import {Component, Input, Output, ComponentRef, Inject} from '@angular/core';
import {Component as IComponent} from 'app/models/components/component';

import {SdcConfigToken, ISdcConfig} from "app/ng2/config/sdc-config.config";

import {Observable} from "rxjs/Observable";

import {ModalComponent} from 'app/ng2/components/ui/modal/modal.component';
import {ModalService} from 'app/ng2/services/modal.service';
import {ModalModel, ButtonModel, InputBEModel, OperationModel, CreateOperationResponse, WORKFLOW_ASSOCIATION_OPTIONS} from 'app/models';

import {ComponentServiceNg2} from 'app/ng2/services/component-services/component.service';
import {ComponentGenericResponse} from 'app/ng2/services/responses/component-generic-response';
import {WorkflowServiceNg2} from 'app/ng2/services/workflow.service';

import {OperationCreatorComponent, OperationCreatorInput} from './operation-creator/operation-creator.component';

@Component({
    selector: 'interface-operation',
    templateUrl: './interface-operation.page.component.html',
    styleUrls: ['interface-operation.page.component.less'],
    providers: [ModalService]
})

export class InterfaceOperationComponent {

    modalInstance: ComponentRef<ModalComponent>;
    operationList: Array<OperationModel> = [];
    openOperation: OperationModel;
    enableWorkflowAssociation: boolean;
    inputs: Array<InputBEModel>;
    isLoading: boolean;

    @Input() component: IComponent;
    @Input() readonly: boolean;
    @Input() enableMenuItems: Function;
    @Input() disableMenuItems: Function;

    constructor(
        @Inject(SdcConfigToken) private sdcConfig: ISdcConfig,
        @Inject("$state") private $state: ng.ui.IStateService,
        private ComponentServiceNg2: ComponentServiceNg2,
        private WorkflowServiceNg2: WorkflowServiceNg2,
        private ModalServiceNg2: ModalService,
    ) {
        this.enableWorkflowAssociation = sdcConfig.enableWorkflowAssociation;
    }

    ngOnInit(): void {
        this.isLoading = true;
        Observable.forkJoin(
            this.ComponentServiceNg2.getInterfaceOperations(this.component),
            this.ComponentServiceNg2.getComponentInputs(this.component)
        ).subscribe((response) => {
            this.isLoading = false;
            this.component.interfaceOperations = response[0].interfaceOperations;
            this.operationList = _.toArray(response[0].interfaceOperations).sort((a, b) => a.operationType.localeCompare(b.operationType));
            this.inputs = response[1].inputs;
        });
    }

    getDisabled = (): boolean => {
        return !this.modalInstance.instance.dynamicContent.instance.checkFormValidForSubmit();
    }

    onEditOperation = (operation?: OperationModel): void => {
        const modalMap = {
            create: {
                modalTitle: 'Create a New Operation',
                saveBtnText: 'Create',
                submitCallback: this.createOperation,
            },
            edit: {
                modalTitle: 'Edit Operation',
                saveBtnText: 'Save',
                submitCallback: this.updateOperation,
            }
        };

        const modalData = operation ? modalMap.edit : modalMap.create;

        if (this.openOperation) {
            if (operation ? operation.uniqueId === this.openOperation.uniqueId : !this.openOperation.uniqueId) {
                operation = this.openOperation;
            }
        }

        const cancelButton: ButtonModel = new ButtonModel(
            'Cancel',
            'outline white',
            () => {
                this.openOperation = null;
                this.ModalServiceNg2.closeCurrentModal();
            },
        );

        const saveButton: ButtonModel = new ButtonModel(
            modalData.saveBtnText,
            'blue',
            () => {
                this.modalInstance.instance.dynamicContent.instance.createParamLists();
                this.ModalServiceNg2.closeCurrentModal();

                const {operation, isUsingExistingWF} = this.modalInstance.instance.dynamicContent.instance;
                this.openOperation = {...operation};

                if (this.enableWorkflowAssociation && !isUsingExistingWF()) {
                    operation.workflowId = null;
                    operation.workflowVersionId = null;
                }

                modalData.submitCallback(operation);
            },
            this.getDisabled,
        );

        const modalModel: ModalModel = new ModalModel(
            'l',
            modalData.modalTitle,
            '',
            [saveButton, cancelButton],
            'standard',
        );

        this.modalInstance = this.ModalServiceNg2.createCustomModal(modalModel);

        let input: OperationCreatorInput = {
            operation,
            inputProperties: this.inputs,
            enableWorkflowAssociation: this.enableWorkflowAssociation,
            readonly: this.readonly,
            isService: this.component.isService()
        }

        this.ModalServiceNg2.addDynamicContentToModal(
            this.modalInstance,
            OperationCreatorComponent,
            input,
        );

        this.modalInstance.instance.open();
    }

    onRemoveOperation = (event: Event, operation: OperationModel): void => {
        event.stopPropagation();

        const confirmCallback = () => {
            this.ModalServiceNg2.closeCurrentModal();
            this.ComponentServiceNg2
                .deleteInterfaceOperation(this.component, operation)
                .subscribe(() => {
                    const index = _.findIndex(this.operationList, el => el.uniqueId === operation.uniqueId);
                    this.operationList.splice(index, 1);
                    this.component.interfaceOperations = this.operationList;
                });
        }

        this.modalInstance = this.ModalServiceNg2.createActionModal(
            operation.operationType,
            'Are you sure you want to delete this operation?',
            'Delete',
            confirmCallback,
            'Cancel',
        );

        this.modalInstance.instance.open();
    }

    private createOperation = (operation: OperationModel): void => {
        this.ComponentServiceNg2.createInterfaceOperation(this.component, operation).subscribe((response: CreateOperationResponse) => {
            this.openOperation = null;
            this.operationList.push(new OperationModel(response));
            this.operationList.sort((a, b) => a.operationType.localeCompare(b.operationType));

            if (response.workflowId && operation.workflowAssociationType === WORKFLOW_ASSOCIATION_OPTIONS.EXISTING) {
                const operationId = response.uniqueId;
                const workflowId = response.workflowId;
                const versionId = response.workflowVersionId;
                const artifactId = response.artifactUUID;
                this.WorkflowServiceNg2.associateWorkflowArtifact(this.component, operationId, workflowId, versionId, artifactId).subscribe();
            } else if (operation.workflowAssociationType === WORKFLOW_ASSOCIATION_OPTIONS.NEW) {
                this.$state.go('workspace.plugins', { path: 'workflowDesigner' });
            }
        });
    }

    private updateOperation = (operation: OperationModel): void => {
        this.ComponentServiceNg2.updateInterfaceOperation(this.component, operation).subscribe(newOperation => {
            this.openOperation = null;
            const index = _.findIndex(this.operationList, el => el.uniqueId === operation.uniqueId);
            this.operationList.splice(index, 1, newOperation);
            this.component.interfaceOperations = this.operationList;

            if (newOperation.workflowId && operation.workflowAssociationType === WORKFLOW_ASSOCIATION_OPTIONS.EXISTING) {
                const operationId = newOperation.uniqueId;
                const workflowId = newOperation.workflowId;
                const versionId = newOperation.workflowVersionId;
                const artifactId = newOperation.artifactUUID;
                this.WorkflowServiceNg2.associateWorkflowArtifact(this.component, operationId, workflowId, versionId, artifactId).subscribe();
            }
        });
    }

}
