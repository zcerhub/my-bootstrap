package ctbiyi.com.service.impl;

import ctbiyi.com.service.LicenseServerService;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;

public class LicenseServerManager extends LicenseManager {

    public LicenseServerManager(LicenseParam licenseParam) {
        super(licenseParam);
    }

    public byte[] getLicenseBytes(LicenseContent licenseContent) throws Exception{

        return create(licenseContent);
    }

    public synchronized LicenseContent cachelessVerify() throws Exception{
        LicenseNotary notary = getLicenseNotary();

        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) certificate.getContent();
        validate(content);
        setCertificate(certificate);

        return content;
    }


}
