import {
    Directive, ComponentRef, ViewContainerRef, ComponentFactoryResolver, Input, HostListener
} from "@angular/core";
import {TooltipContentComponent} from "./tooltip-content.component";

@Directive ({
    selector: "[tooltip]"
})
export class TooltipComponent {

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private tooltip: ComponentRef<TooltipContentComponent>;
    private visible: boolean;
    private delayInProgress: boolean = false;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    constructor(private viewContainerRef: ViewContainerRef,
                private resolver: ComponentFactoryResolver) {
    }

    // -------------------------------------------------------------------------
    // Inputs / Outputs
    // -------------------------------------------------------------------------

    @Input("tooltip") content: string|TooltipContentComponent;
    @Input() tooltipDisabled: boolean;
    @Input() tooltipAnimation: boolean = true;
    @Input() tooltipPlacement: "top"|"bottom"|"left"|"right" = "bottom";
    @Input() tooltipDelay: number = 1500;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    @HostListener("mouseenter")
    show(): void {
        if(this.tooltipDisabled || this.visible || this.content === "") {
            return;
        }
        if (this.tooltipDelay && !this.delayInProgress) {
            this.delayInProgress = true;
            setTimeout(() => { this.delayInProgress && this.show() }, this.tooltipDelay);
            return;
        }

        this.visible = true;
        if (typeof this.content === "string") {
            const factory = this.resolver.resolveComponentFactory(TooltipContentComponent);
            if (!this.visible) {
                return;
            }

            this.tooltip = this.viewContainerRef.createComponent(factory);
            this.tooltip.instance.hostElement = this.viewContainerRef.element.nativeElement;
            this.tooltip.instance.content = this.content as string;
            this.tooltip.instance.placement = this.tooltipPlacement;
            this.tooltip.instance.animation = this.tooltipAnimation;
        } else {
            const tooltip = this.content as TooltipContentComponent;
            tooltip.hostElement = this.viewContainerRef.element.nativeElement;
            tooltip.placement = this.tooltipPlacement;
            tooltip.animation = this.tooltipAnimation;
            tooltip.show();
        }
    }

    @HostListener("mouseleave")
    hide(): void {
        this.delayInProgress = false;
        if (!this.visible) {
            return;
        }

        this.visible = false;
        if (this.tooltip) {
            this.tooltip.destroy();
        }
        if (this.content instanceof TooltipContentComponent) {
            (this.content as TooltipContentComponent).hide();
        }
    }
}
