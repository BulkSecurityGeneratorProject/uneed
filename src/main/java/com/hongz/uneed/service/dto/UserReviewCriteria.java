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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.hongz.uneed.domain.UserReview} entity. This class is used
 * in {@link com.hongz.uneed.web.rest.UserReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserReviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter score;

    private StringFilter comment;

    private ZonedDateTimeFilter date;

    private StringFilter reviewer;

    private LongFilter userId;

    public UserReviewCriteria(){
    }

    public UserReviewCriteria(UserReviewCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.score = other.score == null ? null : other.score.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.reviewer = other.reviewer == null ? null : other.reviewer.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserReviewCriteria copy() {
        return new UserReviewCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getScore() {
        return score;
    }

    public void setScore(IntegerFilter score) {
        this.score = score;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StringFilter getReviewer() {
        return reviewer;
    }

    public void setReviewer(StringFilter reviewer) {
        this.reviewer = reviewer;
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
        final UserReviewCriteria that = (UserReviewCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(score, that.score) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(date, that.date) &&
            Objects.equals(reviewer, that.reviewer) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        score,
        comment,
        date,
        reviewer,
        userId
        );
    }

    @Override
    public String toString() {
        return "UserReviewCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (score != null ? "score=" + score + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (reviewer != null ? "reviewer=" + reviewer + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
