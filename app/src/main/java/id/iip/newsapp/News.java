package id.iip.newsapp;

import java.util.Date;
import java.util.List;

public class News {

    /** variable to represent type of the news */
    private String mType;

    /** variable to represent section name of the news */
    private String mSectionName;

    /** Variable to represent the title of the news */
    private String mWebTitle;

    /** Variable to represent the web site url of the news */
    private String mWebUrl;

    /** Variable to represent the contributor of the news */
    private List<String> mContibutor;

    /** Variable to represent the publication of the news */
    private Date mPublicationDate;

    /**
     * Default contructor to init object News
     * @param mType is type of the news
     * @param mSectionName is section name of the news
     * @param mWebTitle is web title of the news
     * @param mWebUrl is web url name of the news
     * @param
     */
    public News(String mType, String mSectionName, String mWebTitle, String mWebUrl, List<String> mContributor, Date publicationDate) {
        this.mType = mType;
        this.mSectionName = mSectionName;
        this.mWebTitle = mWebTitle;
        this.mWebUrl = mWebUrl;
        this.mContibutor = mContributor;
        this.mPublicationDate = publicationDate;
    }

    // region member variable getter method
    public String getType() {
        return mType;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public List<String> getContibutor() {
        return mContibutor;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }

    //endregion

}
