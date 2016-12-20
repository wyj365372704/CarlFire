/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.app.carlfire.beans;

import android.util.Log;

public class PhotoGirl {
    /**
     * {
     * "desc": "12-5",
     * "ganhuo_id": "5844b8dd421aa939befafb03",
     * "publishedAt": "2016-12-05T11:40:51.351000",
     * "readability": "",
     * "type": "\u798f\u5229",
     * "url": "http://ww4.sinaimg.cn/large/610dc034gw1fafmi73pomj20u00u0abr.jpg",
     * "who": "daimajia"
     * }
     */

    private String desc;
    private String ganhuo_id;
    private String publishedAt;
    private String readability;
    private String type;
    private String url;
    private String who;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGanhuo_id() {
        return ganhuo_id;
    }

    public void setGanhuo_id(String ganhuo_id) {
        this.ganhuo_id = ganhuo_id;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof PhotoGirl && getGanhuo_id().equals(((PhotoGirl) obj).getGanhuo_id())) {
            return true;
        } else {
            return false;
        }
    }
}
