package com.hongz.uneed.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A UserStat.
 */
@Entity
@Table(name = "user_stat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "rating", precision = 21, scale = 2)
    private BigDecimal rating;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public UserStat viewCount(Long viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public UserStat reviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
        return this;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public UserStat rating(BigDecimal rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public UserStat user(User user) {
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
        if (!(o instanceof UserStat)) {
            return false;
        }
        return id != null && id.equals(((UserStat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserStat{" +
            "id=" + getId() +
            ", viewCount=" + getViewCount() +
            ", reviewCount=" + getReviewCount() +
            ", rating=" + getRating() +
            "}";
    }
}
