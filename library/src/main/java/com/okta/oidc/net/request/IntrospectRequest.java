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

package com.okta.oidc.net.request;

import android.net.Uri;

import androidx.annotation.RestrictTo;

import com.google.gson.Gson;
import com.okta.oidc.net.ConnectionParameters;
import com.okta.oidc.net.HttpResponse;
import com.okta.oidc.net.OktaHttpClient;
import com.okta.oidc.net.response.IntrospectInfo;
import com.okta.oidc.util.AuthorizationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class IntrospectRequest extends
        BaseRequest<IntrospectInfo, AuthorizationException> {
    IntrospectRequest(HttpRequestBuilder.Introspect b) {
        super();
        mRequestType = b.mRequestType;
        mUri = Uri.parse(b.mProviderConfiguration.introspection_endpoint).buildUpon()
                .appendQueryParameter("client_id", b.mConfig.getClientId())
                .appendQueryParameter("token", b.mIntrospectToken)
                .appendQueryParameter("token_type_hint", b.mTokenTypeHint)
                .build();
        mConnParams = new ConnectionParameters.ParameterBuilder()
                .setRequestMethod(ConnectionParameters.RequestMethod.POST)
                .setRequestType(mRequestType)
                .create();
    }

    @Override
    public IntrospectInfo executeRequest(OktaHttpClient client) throws AuthorizationException {
        AuthorizationException exception = null;
        HttpResponse response = null;
        try {
            response = openConnection(client);
            JSONObject json = response.asJson();
            return new Gson().fromJson(json.toString(), IntrospectInfo.class);
        } catch (IOException ex) {
            exception = new AuthorizationException(ex.getMessage(), ex);
        } catch (JSONException e) {
            exception = AuthorizationException.fromTemplate(
                    AuthorizationException.GeneralErrors.JSON_DESERIALIZATION_ERROR,
                    e);
        } catch (Exception e) {
            exception = AuthorizationException.fromTemplate(AuthorizationException
                    .GeneralErrors.NETWORK_ERROR, e);
        } finally {
            if (response != null) {
                response.disconnect();
            }
            if (exception != null) {
                throw exception;
            }
        }
        return null;
    }
}
