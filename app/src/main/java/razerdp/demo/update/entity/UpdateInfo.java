package razerdp.demo.update.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 大灯泡 on 2018/3/8.
 */
public class UpdateInfo {

    /**
     * name : fir.im
     * version : 1.0
     * changelog : 更新日志
     * versionShort : 1.0.5
     * build : 6
     * installUrl : http://download.fir.im/v2/app/install/xxxxxxxxxxxxxxxxxxxx?download_token=xxxxxxxxxxxxxxxxxxxxxxxxxxxx
     * install_url : http://download.fir.im/v2/app/install/xxxxxxxxxxxxxxxx?download_token=xxxxxxxxxxxxxxxxxxxxxxxxxxxx
     * update_url : http://fir.im/fir
     * binary : {"fsize":6446245}
     */

    @SerializedName("name")
    private String mName;
    @SerializedName("version")
    private String mVersion;
    @SerializedName("changelog")
    private String mChangelog;
    @SerializedName("versionShort")
    private String mVersionShort;
    @SerializedName("build")
    private int mBuild;
    @SerializedName("installUrl")
    private String mInstallUrl2;
    @SerializedName("install_url")
    private String mInstallUrl;
    @SerializedName("update_url")
    private String mUpdateUrl;
    @SerializedName("binary")
    private BinaryBean mBinary;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getChangelog() {
        return mChangelog;
    }

    public void setChangelog(String changelog) {
        mChangelog = changelog;
    }

    public String getVersionShort() {
        return mVersionShort;
    }

    public void setVersionShort(String versionShort) {
        mVersionShort = versionShort;
    }

    public int getBuild() {
        return mBuild;
    }

    public void setBuild(int build) {
        mBuild = build;
    }

    public String getInstallUrl2() {
        return mInstallUrl2;
    }

    public void setInstallUrl2(String installUrl2) {
        mInstallUrl2 = installUrl2;
    }

    public String getInstallUrl() {
        return mInstallUrl;
    }

    public void setInstallUrl(String installUrl) {
        mInstallUrl = installUrl;
    }

    public String getUpdateUrl() {
        return mUpdateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        mUpdateUrl = updateUrl;
    }

    public BinaryBean getBinary() {
        return mBinary;
    }

    public void setBinary(BinaryBean binary) {
        mBinary = binary;
    }
}
