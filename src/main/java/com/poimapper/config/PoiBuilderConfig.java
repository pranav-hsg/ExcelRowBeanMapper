package com.poimapper.config;

import com.poimapper.constants.CastConstants;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PoiBuilderConfig {
    private boolean suppressWarnings;
    private boolean strictMode;

    public PoiBuilderConfig(){
        this.suppressWarnings = CastConstants.SUPPRESS_WARNINGS;
        this.strictMode = CastConstants.STRICT_MODE;
    }
}
