package com.sccreporte.reporte.data;

public class SocialMediaData {
    public int OneSocialImage;
    public int TwoSocialImage;
    public int ThreeSocialImage;

    public String OneSocialName;
    public String TwoSocialName;
    public String ThreeSocialName;

    public String OneSocialLink;
    public String TwoSocialLink;
    public String ThreeSocialLink;

    public SocialMediaData(int oneSocialImage, int twoSocialImage, int threeSocialImage,
                           String oneSocialName, String twoSocialName, String threeSocialName,
                           String oneSocialLink, String twoSocialLink, String threeSocialLink){
        OneSocialImage = oneSocialImage;
        TwoSocialImage = twoSocialImage;
        ThreeSocialImage = threeSocialImage;
        OneSocialName = oneSocialName;
        TwoSocialName = twoSocialName;
        ThreeSocialName = threeSocialName;
        OneSocialLink = oneSocialLink;
        TwoSocialLink = twoSocialLink;
        ThreeSocialLink = threeSocialLink;
    }
}
