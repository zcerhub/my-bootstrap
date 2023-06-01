package ctbiyi.com.service;

import ctbiyi.com.dto.LicenseServerDTO;

public interface LicenseServerService {
    byte[] generatLicense(LicenseServerDTO licenseCreateDTO) ;
}
