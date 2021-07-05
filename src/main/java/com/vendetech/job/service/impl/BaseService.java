package com.vendetech.job.service.impl;

import com.vendetech.hr.service.impl.kit.FddApi;

public class BaseService {

    protected FddApi getFddApi(String appId, String appSecret, String version, String host) {
        return new FddApi(appId, appSecret, version, host);
    }
}
