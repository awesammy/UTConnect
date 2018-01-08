package utconnect.samapplab.com.utconnect;

/**
 * Created by samchen on 2018-01-06.
 */

public class Contact {

    private String contactName;
    private String contactID;
    private String contactType;
    private String contactAddress;
    private String contactStatus;
    private String contactImageUrl;
    private String contactEmail;
    private String contactPhone;
    private String contactWebURL;
    private String contactCampus;

    public Contact(String contactName, String contactID, String contactType, String contactAddress, String contactStatus, String contactImageUrl, String contactEmail, String contactPhone, String contactWebURL, String contactCampus) {
        this.contactName = contactName;
        this.contactID = contactID;
        this.contactType = contactType;
        this.contactAddress = contactAddress;
        this.contactStatus = contactStatus;
        this.contactImageUrl = contactImageUrl;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactWebURL = contactWebURL;
        this.contactCampus = contactCampus;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getContactImageUrl() {
        return contactImageUrl;
    }

    public void setContactImageUrl(String contactImageUrl) {
        this.contactImageUrl = contactImageUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactWebURL() {
        return contactWebURL;
    }

    public void setContactWebURL(String contactWebURL) {
        this.contactWebURL = contactWebURL;
    }

    public String getContactCampus() {
        return contactCampus;
    }

    public void setContactCampus(String contactCampus) {
        this.contactCampus = contactCampus;
    }
}
