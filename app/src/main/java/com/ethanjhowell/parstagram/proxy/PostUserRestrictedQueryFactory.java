package com.ethanjhowell.parstagram.proxy;

import com.ethanjhowell.parstagram.models.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PostUserRestrictedQueryFactory extends PostQueryFactory {
    @Override
    public ParseQuery<Post> query(int limit, int startPos) {
        ParseQuery<Post> query = super.query(limit, startPos);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        return query;
    }
}
