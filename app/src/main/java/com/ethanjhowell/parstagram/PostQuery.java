package com.ethanjhowell.parstagram;

import com.parse.ParseQuery;

public interface PostQuery {
    static ParseQuery<Post> query(int limit, int startPos) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        // order posts by creation date (newest first)
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.setLimit(limit);
        query.setSkip(startPos);
        return query;
    }
}
