package com.dap.paas.console.basic.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseMetadataDTO {

    private String name;

    private String namespace;

    private String uid;

    private String resourceVersion;
}
