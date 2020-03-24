package com.tom.template.security.oauth2.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

	final Map<String, Object> attributes;

    OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    protected abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
    
    public String getFirstName() {
		String[] name = getName().split(" ");
		if (name.length > 0) {
			return name[0];
		} 
		return null;
 	}

	public String getLastName() {
		String[] name = getName().split(" ");
		if (name.length >= 2) {
			return name[name.length - 1];
		} 
		return null;
	}

}
