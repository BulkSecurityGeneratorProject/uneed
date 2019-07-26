package com.hongz.uneed.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.hongz.uneed.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.hongz.uneed.domain.UserInfo} entity. This class is used
 * in {@link com.hongz.uneed.web.rest.UserInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserInfoCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phone;

    private StringFilter mobilePhone;

    private BooleanFilter emailFlag;

    private BooleanFilter smsFlag;

    private LocalDateFilter birthDate;

    private GenderFilter gender;

    private LongFilter userId;

    public UserInfoCriteria(){
    }

    public UserInfoCriteria(UserInfoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.mobilePhone = other.mobilePhone == null ? null : other.mobilePhone.copy();
        this.emailFlag = other.emailFlag == null ? null : other.emailFlag.copy();
        this.smsFlag = other.smsFlag == null ? null : other.smsFlag.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserInfoCriteria copy() {
        return new UserInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(StringFilter mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public BooleanFilter getEmailFlag() {
        return emailFlag;
    }

    public void setEmailFlag(BooleanFilter emailFlag) {
        this.emailFlag = emailFlag;
    }

    public BooleanFilter getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(BooleanFilter smsFlag) {
        this.smsFlag = smsFlag;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
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
        final UserInfoCriteria that = (UserInfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(mobilePhone, that.mobilePhone) &&
            Objects.equals(emailFlag, that.emailFlag) &&
            Objects.equals(smsFlag, that.smsFlag) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        phone,
        mobilePhone,
        emailFlag,
        smsFlag,
        birthDate,
        gender,
        userId
        );
    }

    @Override
    public String toString() {
        return "UserInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (mobilePhone != null ? "mobilePhone=" + mobilePhone + ", " : "") +
                (emailFlag != null ? "emailFlag=" + emailFlag + ", " : "") +
                (smsFlag != null ? "smsFlag=" + smsFlag + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
