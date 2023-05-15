package main.java.wavemark.controller;

/**
 * @author nawar.khoury
 */
public class Directories {
    
    public static final String CIMSHUB_HOME = System.getenv("CIMSHUB_HOME");
    public static final String WILDFLY_HOME = System.getenv("JBOSS_HOME") != null ? System.getenv("JBOSS_HOME")
                                                  : CIMSHUB_HOME + "/../wildfly-21.0.2.Final";
    
    public static final String LOOKUP = CIMSHUB_HOME + "/lookup";
    public static final String LOGS = CIMSHUB_HOME + "/logs";
    public static final String CONFIG_TEMPLATES = CIMSHUB_HOME + "/config_templates";
    public static final String DATA = CIMSHUB_HOME + "/data";
    public static final String INTERFACES_METADATA = DATA + "/intefaces_metadata";
    public static final String DYNAMIC_DATAMAPS = INTERFACES_METADATA + "/dynamic_datamaps";
    public static final String DATAMAPS_SAMPLE_FILES = INTERFACES_METADATA + "/datamap_sample_files";
    public static final String TRIGGERS = DATA + "/triggers";
    public static final String PATH_CONFIG = CIMSHUB_HOME + "/pathConfig";
    public static final String PATH_CONFIG_DATAMAPS = PATH_CONFIG + "/datamaps";
    public static final String INTERFACES = CIMSHUB_HOME + "/Interfaces";
    public static final String UNINSTALLED_INTERFACES = CIMSHUB_HOME + "/UninstalledInterfaces";
    public static final String SCRIPTS = CIMSHUB_HOME + "/scripts";
    public static final String SCRIPTS_MUI = CIMSHUB_HOME + "/scripts/ManagementUI";
    public static final String SETTINGS = CIMSHUB_HOME + "/settings";
    public static final String TRANSMIT = CIMSHUB_HOME + "/transmit";
    public static final String AGENT = CIMSHUB_HOME + "/../Agent";
    public static final String WILDFLYLOGS = WILDFLY_HOME + "/standalone/log";
}
