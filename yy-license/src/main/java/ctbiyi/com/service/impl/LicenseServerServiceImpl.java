package ctbiyi.com.service.impl;


import ctbiyi.com.bo.LicenseCustomContent;
import ctbiyi.com.dto.LicenseServerDTO;
import ctbiyi.com.service.LicenseServerService;
import ctbiyi.com.web.LicenseConfigBean;
import de.schlichtherle.license.*;
import io.reactivex.rxjava3.internal.operators.observable.ObservableJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.util.Date;
import java.util.prefs.Preferences;

@Service
public class LicenseServerServiceImpl implements LicenseServerService {

    @Autowired
    private LicenseConfigBean licenseConfigBean;

    @Value("${yy.license.limit}")
    private int yyLimit;

    @Override
    public byte[] generatLicense(LicenseServerDTO licenseCreateDTO) {
        LicenseParam licenseParam=createLicenseParam();
        LicenseServerManager licenseServerManager = new LicenseServerManager(licenseParam);
        LicenseCustomContent licenseCustomContent = createLicenseCustomContent(licenseCreateDTO);
        LicenseContent licenseContent = createLicenseContent(licenseCreateDTO.getExpireTime(), licenseCustomContent);

        try {
            return licenseServerManager.getLicenseBytes(licenseContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LicenseCustomContent createLicenseCustomContent(LicenseServerDTO licenseCreateDTO) {
        return LicenseCustomContent.builder().macs(licenseCreateDTO.getMacs())
                .limit(yyLimit).build();
    }

    private LicenseParam createLicenseParam() {
        Class<LicenseServerServiceImpl> licenseServerClass = LicenseServerServiceImpl.class;
        Preferences preferences = Preferences.userNodeForPackage(licenseServerClass);
        CipherParam cipherParam = new DefaultCipherParam(licenseConfigBean.getKeyPwd());
        KeyStoreParam keyStoreParam=new DefaultKeyStoreParam(licenseServerClass,
                licenseConfigBean.getPrivateStorePath(),
                licenseConfigBean.getPrivateAlias(),
                licenseConfigBean.getStorePwd(),
                licenseConfigBean.getKeyPwd());

        LicenseParam licenseParam = new DefaultLicenseParam(licenseConfigBean.getSubject() , preferences, keyStoreParam, cipherParam);
        return licenseParam;
    }

    private LicenseContent createLicenseContent(Date expireTime, Object extraContent) {

        X500Principal holderAndIssue = new X500Principal(licenseConfigBean.getDname());

        LicenseContent content=new LicenseContent();
        Date currentTime = new Date();
        content.setSubject(licenseConfigBean.getSubject());
        content.setHolder(holderAndIssue);
        content.setIssued(currentTime);
        content.setIssuer(holderAndIssue);
        content.setNotBefore(currentTime);
        content.setNotAfter(expireTime);
        content.setExtra(extraContent);
        return content;
    }


}
