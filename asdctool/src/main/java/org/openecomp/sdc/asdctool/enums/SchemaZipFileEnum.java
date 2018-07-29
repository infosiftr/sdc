package org.openecomp.sdc.asdctool.enums;

import static org.openecomp.sdc.asdctool.enums.SchemaZipFileEnum.SchemaZipConstants.*;

public enum SchemaZipFileEnum {

    DATA("data", "data-types", "dataTypes", "data_types", EMPTY_IMPORT_LIST),
    GROUPS("groups", "group-types", "groupTypes", "group_types", DATA_IMPORT_LIST),
    POLICIES("policies", "policy-types", "policyTypes", "policy_types", DATA_IMPORT_LIST),
    ANNOTATIONS("annotations", "annotation-types", "annotationTypes", "annotation_types", DATA_IMPORT_LIST),
    RELATIONSHIPS("relationships", "relationship-types", "relationshipTypes", "relationship_types", RELATIONSHIPS_TYPES_IMPORT_LIST),
    ARTIFACTS("artifacts", "artifact-types", "artifactTypes", "artifact_types", DATA_IMPORT_LIST),
    CAPABILITIES("capabilities", "capability-types", "capabilityTypes", "capability_types", DATA_IMPORT_LIST),
    INTERFACES("interfaces", "interface-lifecycle-types", "interfaceLifecycleTypes", "interface_types", DATA_IMPORT_LIST);

    private String fileName;
    private String sourceFolderName;
    private String sourceFileName;
    private String collectionTitle;
    private String[] importFileList;

    SchemaZipFileEnum(String fileName, String sourceFolderName, String sourceFileName, String collectionTitle,
                      String[] importFileList) {
        this.fileName = fileName;
        this.sourceFolderName = sourceFolderName;
        this.sourceFileName = sourceFileName;
        this.collectionTitle = collectionTitle;
        this.importFileList = importFileList;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSourceFolderName() {
        return sourceFolderName;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public String[] getImportFileList() {
        return importFileList;
    }

    static class SchemaZipConstants {
        static final String [] EMPTY_IMPORT_LIST =  new String[]{};
        static final String [] DATA_IMPORT_LIST =  new String[]{"data.yml"};
        static final String [] RELATIONSHIPS_TYPES_IMPORT_LIST = new String[]{"capabilities.yml", "data.yml", "interfaces.yml"};
    }

}
