package com.senkiv.interview.assignment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "txId"})})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String txId;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="userId")
    @JsonIdentityReference(alwaysAsId=true)
    private User user;
    private BigDecimal txAmount;
    private String txAmountCy;
    private BigDecimal fee;
    private String feeCy;
    @Enumerated(EnumType.STRING)
    private TransactionFeeMode feeMode;
    private String authCode;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;


    public enum TransactionFeeMode {
        //add
        A,
        //deduct
        D,;
    }

    public enum TransactionStatus {
        COMPLETED,
        PENDING,
        CANCELED,
        FAILED
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public String getTxAmountCy() {
        return txAmountCy;
    }

    public void setTxAmountCy(String txAmountCy) {
        this.txAmountCy = txAmountCy;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeCy() {
        return feeCy;
    }

    public void setFeeCy(String feeCy) {
        this.feeCy = feeCy;
    }

    public TransactionFeeMode getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(TransactionFeeMode feeMode) {
        this.feeMode = feeMode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTxId(), that.getTxId()) &&
                Objects.equals(getAuthCode(), that.getAuthCode()) &&
                getTransactionStatus() == that.getTransactionStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTxId(), getAuthCode(), getTransactionStatus());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", txId='" + txId + '\'' +
                ", txAmount=" + txAmount +
                ", txAmountCy='" + txAmountCy + '\'' +
                ", fee=" + fee +
                ", feeCy='" + feeCy + '\'' +
                ", feeMode=" + feeMode +
                ", authCode='" + authCode + '\'' +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}
