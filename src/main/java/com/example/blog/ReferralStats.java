package com.example.blog;

public class ReferralStats {
    private String searchEngine;
    private long count;

    public ReferralStats(String searchEngine, long count) {
        this.searchEngine = searchEngine;
        this.count = count;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
