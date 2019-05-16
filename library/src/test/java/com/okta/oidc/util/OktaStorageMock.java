/*
 * Copyright (c) 2019, Okta, Inc. and/or its affiliates. All rights reserved.
 * The Okta software accompanied by this notice is provided pursuant to the Apache License,
 * Version 2.0 (the "License.")
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.okta.oidc.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.okta.oidc.storage.SimpleOktaStorage;

public class OktaStorageMock extends SimpleOktaStorage {
    private boolean mRequireHardware;

    public OktaStorageMock(Context context, boolean hardware) {
        this(context, null, hardware);
    }

    public OktaStorageMock(Context context, String prefName, boolean hardware) {
        super(context, prefName);
        mRequireHardware = hardware;
    }

    @Override
    public boolean requireHardwareBackedKeyStore() {
        return mRequireHardware;
    }

    public SharedPreferences getSharedPreferences() {
        return prefs;
    }
}
