package com.hongz.uneed.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.hongz.uneed.domain.UserStat} entity. This class is used
 * in {@link com.hongz.uneed.web.rest.UserStatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-stats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserStatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter viewCount;

    private IntegerFilter reviewCount;

    private BigDecimalFilter rating;

    private LongFilter userId;

    public UserStatCriteria(){
    }

    public UserStatCriteria(UserStatCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.viewCount = other.viewCount == null ? null : other.viewCount.copy();
        this.reviewCount = other.reviewCount == null ? null : other.reviewCount.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserStatCriteria copy() {
        return new UserStatCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getViewCount() {
        return viewCount;
    }

    public void setViewCount(LongFilter viewCount) {
        this.viewCount = viewCount;
    }

    public IntegerFilter getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(IntegerFilter reviewCount) {
        this.reviewCount = reviewCount;
    }

    public BigDecimalFilter getRating() {
        return rating;
    }

    public void setRating(BigDecimalFilter rating) {
        this.rating = rating;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserStatCriteria that = (UserStatCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(viewCount, that.viewCount) &&
            Objects.equals(reviewCount, that.reviewCount) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        viewCount,
        reviewCount,
        rating,
        userId
        );
    }

    @Override
    public String toString() {
        return "UserStatCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (viewCount != null ? "viewCount=" + viewCount + ", " : "") +
                (reviewCount != null ? "reviewCount=" + reviewCount + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
