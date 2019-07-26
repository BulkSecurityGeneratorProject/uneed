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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.hongz.uneed.domain.UserJob} entity. This class is used
 * in {@link com.hongz.uneed.web.rest.UserJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserJobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BigDecimalFilter price;

    private StringFilter currency;

    private StringFilter imageUrl;

    private ZonedDateTimeFilter createDate;

    private ZonedDateTimeFilter lastUpdateDate;

    private LongFilter userId;

    public UserJobCriteria(){
    }

    public UserJobCriteria(UserJobCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastUpdateDate = other.lastUpdateDate == null ? null : other.lastUpdateDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserJobCriteria copy() {
        return new UserJobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ZonedDateTimeFilter getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTimeFilter createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTimeFilter getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTimeFilter lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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
        final UserJobCriteria that = (UserJobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        price,
        currency,
        imageUrl,
        createDate,
        lastUpdateDate,
        userId
        );
    }

    @Override
    public String toString() {
        return "UserJobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (currency != null ? "currency=" + currency + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (lastUpdateDate != null ? "lastUpdateDate=" + lastUpdateDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
