package com.shellming.utils;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.identity.Access;
import org.openstack4j.model.storage.object.SwiftAccount;
import org.openstack4j.model.storage.object.SwiftContainer;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 11/25/2015.
 */
public class ObjectStorageUtil {

    private OSClient os;
    private final String CONTAINER = "default";

    private Access access;

    public static void main(String[] args) {
//        Configuration hconf = context.hadoopConfiguration();
//        String name = "sparkathon";
//        String prefix = "fs.swift.service." + name;
//        String auth_url = "https://identity.open.softlayer.com/v3";
//        String username = "c43f8662cd9e476ab78809def031589d";
//        String password = "fqSrU(6fo}WDS8Z7";
//        hconf.set(prefix + ".auth.url", auth_url);
//        hconf.set(prefix + ".username", username);
//        hconf.set(prefix + ".tenant", username);
//        hconf.set(prefix + ".auth.endpoint.prefix", "endpoints");
//        hconf.setInt(prefix + ".http.port", 8080);
//        hconf.set(prefix + ".apikey", password);
//        hconf.setBoolean(prefix + ".public", True);
//        hconf.set(prefix + ".use.get.auth", "true");
//        hconf.setBoolean(prefix + ".location-aware", False);
//        hconf.set(prefix + ".password", password);
    }

    public ObjectStorageUtil() {
        String auth_url = "https://identity.open.softlayer.com/v3";
        String username = "c43f8662cd9e476ab78809def031589d";
        String password = "fqSrU(6fo}WDS8Z7";
        String domain = "801153";
        String project = "object_storage_07f8784c";
        Identifier domainIdent = Identifier.byName(domain);
        Identifier projectIdent = Identifier.byName(project);

        os = OSFactory.builderV3()
                .endpoint(auth_url)
                .credentials(username, password)
                .scopeToProject(projectIdent, domainIdent)
                .authenticate();
        access = os.getAccess();
    }

    public Access getAccess(){
        return access;
    }

    public void setOSClient(OSClient client){
        os = client;
    }

    // return the tag of file, useless
    public String put(String key, String value){
        return os.objectStorage().objects().
                put(CONTAINER, key, Payloads.create(IOUtils.toInputStream(value)));
    }

    public String get(String key){
        SwiftObject object = os.objectStorage().objects().get(CONTAINER, key);
        if(object == null)
            return "";
        DLPayload dlPayload = object.download();
        InputStream inputStream = dlPayload.getInputStream();
        try {
            String result = IOUtils.toString(inputStream);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    // list all keys avaliable
    public List<String> list(){
        List<? extends SwiftObject> objs = os.objectStorage().objects().list(CONTAINER);
        List<String> results = new ArrayList<>();
        for(SwiftObject object : objs){
            results.add(object.getName());
        }
        return results;
    }

    public void append(String key, String value){
        String older = get(key);
        if(older == null)
            older = "";
        String result = older + value;
        put(key, result);
    }


    public void delete(String key){
        os.objectStorage().objects().delete(CONTAINER, key);
    }

}
