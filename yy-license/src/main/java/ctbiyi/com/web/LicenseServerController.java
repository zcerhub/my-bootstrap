package ctbiyi.com.web;

import ctbiyi.com.dto.LicenseServerDTO;
import ctbiyi.com.service.LicenseServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("api")
public class LicenseServerController {

    @Autowired
    private LicenseServerService licenseServerService;
    @Autowired
    private LicenseConfigBean licenseConfigBean;

    @PostMapping("license")
    public ResponseEntity<Resource> generatLicense(@RequestBody LicenseServerDTO licenseCreateDTO) {
        byte[] licenseArr = licenseServerService.generatLicense(licenseCreateDTO);
        return createResourceResponseEntity(licenseArr);
    }

    private ResponseEntity<Resource> createResourceResponseEntity(byte[] licenseArr) {
        String contenDispositon= ContentDisposition.builder("attachment").
                filename(licenseConfigBean.getLicenseFileName()).build().toString();
        Resource licenseIns = new ByteArrayResource(licenseArr);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,"application/force-download")
                .header(HttpHeaders.CONTENT_DISPOSITION,contenDispositon)
                .body(licenseIns);
    }


}
