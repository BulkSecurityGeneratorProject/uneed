package com.hongz.uneed.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A UserReview.
 */
@Entity
@Table(name = "user_review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "reviewer")
    private String reviewer;

    @ManyToOne
    @JsonIgnoreProperties("userReviews")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public UserReview score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public UserReview comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public UserReview date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getReviewer() {
        return reviewer;
    }

    public UserReview reviewer(String reviewer) {
        this.reviewer = reviewer;
        return this;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public User getUser() {
        return user;
    }

    public UserReview user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReview)) {
            return false;
        }
        return id != null && id.equals(((UserReview) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserReview{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", comment='" + getComment() + "'" +
            ", date='" + getDate() + "'" +
            ", reviewer='" + getReviewer() + "'" +
            "}";
    }
}
