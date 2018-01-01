package utconnect.samapplab.com.utconnect;

/**
 * Created by samchen on 2017-12-29.
 */

public class Service {

    private String serviceName;
    private String serviceID;
    private String serviceTime;
    private String serviceType;
    private String serviceAddress;
    private String serviceStatus;
    private String serviceStatusCode;
    private String serviceImageUrl;
    private String serviceLongitude;
    private String serviceLatitude;
    private String serviceCampus;




    public Service(String serviceName, String serviceTime, String serviceType, String serviceAddress, String serviceStatus, String serviceStatusCode, String serviceImageUrl, String serviceLongitude, String serviceLatitude, String serviceCampus, String serviceID) {
        this.serviceName = serviceName;
        this.serviceTime = serviceTime;
        this.serviceType = serviceType;
        this.serviceAddress = serviceAddress;
        this.serviceStatus = serviceStatus;
        this.serviceStatusCode = serviceStatusCode;
        this.serviceImageUrl = serviceImageUrl;
        this.serviceLongitude = serviceLongitude;
        this.serviceLatitude = serviceLatitude;
        this.serviceCampus = serviceCampus;
        this.serviceID = serviceID;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public void setServiceStatusCode(String serviceStatusCode) {
        this.serviceStatusCode = serviceStatusCode;
    }

    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImageUrl = serviceImageUrl;
    }

    public void setServiceLongitude(String serviceLongitude) {
        this.serviceLongitude = serviceLongitude;
    }

    public void setServiceLatitude(String serviceLatitude) {
        this.serviceLatitude = serviceLatitude;
    }

    public String getServiceName() {

        return serviceName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public String getServiceStatusCode() {
        return serviceStatusCode;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }

    public String getServiceLongitude() {
        return serviceLongitude;
    }

    public String getServiceLatitude() {
        return serviceLatitude;
    }

    public String getServiceCampus() {
        return serviceCampus;
    }

    public void setServiceCampus(String serviceCampus) {
        this.serviceCampus = serviceCampus;
    }
    public String getServiceID() {
        return serviceID;
    }
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
