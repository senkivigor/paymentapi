package com.senkiv.interview.assignment.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.senkiv.interview.assignment.domain.entity.User;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyUserResponseDTO {
    private String userId;
    private Boolean success;
    private String userCat;
    private String kycStatus;
    private User.Sex sex;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String zip;
    private String country;
    private String email;
    private String dob;
    private String mobile;
    private BigDecimal balance;
    private String balanceCy;
    private String locale;
    private Integer errCode;
    private String errMsg;

    private VerifyUserResponseDTO(VerifyUserResponseDTOBuilder builder) {
        userId = builder.userId;
        success = builder.success;
        userCat = builder.userCat;
        kycStatus = builder.kycStatus;
        sex = builder.sex;
        firstName = builder.firstName;
        lastName = builder.lastName;
        street = builder.street;
        city = builder.city;
        zip = builder.zip;
        country = builder.country;
        email = builder.email;
        dob = builder.dob;
        mobile = builder.mobile;
        balance = builder.balance;
        balanceCy = builder.balanceCy;
        locale = builder.locale;
        errCode = builder.errCode;
        errMsg = builder.errMsg;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean isSuccess() {
        return success;
    }

    public String getUserCat() {
        return userCat;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public User.Sex getSex() {
        return sex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getMobile() {
        return mobile;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getBalanceCy() {
        return balanceCy;
    }

    public String getLocale() {
        return locale;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public String toString() {
        return "VerifyUserResponseDTO{" + "userId='" + userId + '\'' + ", success=" + success + ", userCat='" + userCat + '\'' + ", kycStatus='"
                + kycStatus + '\'' + ", sex=" + sex + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", street='"
                + street + '\'' + ", city='" + city + '\'' + ", zip='" + zip + '\'' + ", country='" + country + '\'' + ", email='" + email + '\''
                + ", dob='" + dob + '\'' + ", mobile='" + mobile + '\'' + ", balance=" + balance + ", balanceCy='" + balanceCy + '\''
                + ", locale='" + locale + '\'' + ", errCode=" + errCode + ", errMsg='" + errMsg + '\'' + '}';
    }

    public static VerifyUserResponseDTOBuilder verifyUserResponseDTO() {
        return new VerifyUserResponseDTOBuilder();
    }

    public static class VerifyUserResponseDTOBuilder {
        private String userId;
        private Boolean success;
        private String userCat;
        private String kycStatus;
        private User.Sex sex;
        private String firstName;
        private String lastName;
        private String street;
        private String city;
        private String zip;
        private String country;
        private String email;
        private String dob;
        private String mobile;
        private BigDecimal balance;
        private String balanceCy;
        private String locale;
        private Integer errCode;
        private String errMsg;

        private VerifyUserResponseDTOBuilder() {
        }

        public VerifyUserResponseDTOBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public VerifyUserResponseDTOBuilder withSuccess(Boolean success) {
            this.success = success;
            return this;
        }

        public VerifyUserResponseDTOBuilder withUserCat(String userCat) {
            this.userCat = userCat;
            return this;
        }

        public VerifyUserResponseDTOBuilder withKycStatus(String kycStatus) {
            this.kycStatus = kycStatus;
            return this;
        }

        public VerifyUserResponseDTOBuilder withSex(User.Sex sex) {
            this.sex = sex;
            return this;
        }

        public VerifyUserResponseDTOBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public VerifyUserResponseDTOBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public VerifyUserResponseDTOBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public VerifyUserResponseDTOBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public VerifyUserResponseDTOBuilder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public VerifyUserResponseDTOBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public VerifyUserResponseDTOBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public VerifyUserResponseDTOBuilder withDob(String dob) {
            this.dob = dob;
            return this;
        }

        public VerifyUserResponseDTOBuilder withMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public VerifyUserResponseDTOBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public VerifyUserResponseDTOBuilder withBalanceCy(String balanceCy) {
            this.balanceCy = balanceCy;
            return this;
        }

        public VerifyUserResponseDTOBuilder withLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public VerifyUserResponseDTOBuilder withErrCode(Integer errCode) {
            this.errCode = errCode;
            return this;
        }

        public VerifyUserResponseDTOBuilder withErrMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public VerifyUserResponseDTO build() {
            return new VerifyUserResponseDTO(this);
        }
    }
}
