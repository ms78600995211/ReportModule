/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

/**
 *
 * @author lENOVO
 */
public class ReportBean {
    
    private int report_table_id;
    private int report_type_id;
    private int report_status_id;
    private String report_name;
    private String sample_report_path;
    private String jrxml_file_name;
    private String api_json_file_path;
    private int no_of_variables;
    private String active;
    private int revision_no;
    private String created_at;
    private String created_by;
    private String description;
    private String remark;
    private String report_status_name;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReport_status_name() {
        return report_status_name;
    }

    public int getRevision_no() {
        return revision_no;
    }

    public int getNo_of_variables() {
        return no_of_variables;
    }

    public void setNo_of_variables(int no_of_variables) {
        this.no_of_variables = no_of_variables;
    }

    public void setRevision_no(int revision_no) {
        this.revision_no = revision_no;
    }

    public void setReport_status_name(String report_status_name) {
        this.report_status_name = report_status_name;
    }

    public int getReport_table_id() {
        return report_table_id;
    }

    public void setReport_table_id(int report_table_id) {
        this.report_table_id = report_table_id;
    }

    public int getReport_type_id() {
        return report_type_id;
    }

    public void setReport_type_id(int report_type_id) {
        this.report_type_id = report_type_id;
    }

    public int getReport_status_id() {
        return report_status_id;
    }

    public void setReport_status_id(int report_status_id) {
        this.report_status_id = report_status_id;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getSample_report_path() {
        return sample_report_path;
    }

    public void setSample_report_path(String sample_report_path) {
        this.sample_report_path = sample_report_path;
    }

    public String getJrxml_file_name() {
        return jrxml_file_name;
    }

    public void setJrxml_file_name(String jrxml_file_name) {
        this.jrxml_file_name = jrxml_file_name;
    }

    public String getApi_json_file_path() {
        return api_json_file_path;
    }

    public void setApi_json_file_path(String api_json_file_path) {
        this.api_json_file_path = api_json_file_path;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }                
}
