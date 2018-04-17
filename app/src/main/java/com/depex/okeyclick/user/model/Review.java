package com.depex.okeyclick.user.model;


import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.Date;

public class Review {

    @SerializedName("id")
    private String id;
    @SerializedName("task_id")
    private String taskId;
    @SerializedName("sender")
    private String sender;
    @SerializedName("sender_image")
    private String senderImage;
    @SerializedName("comment")
    private String comment;
    @SerializedName("rating")
    private String rating;
    @SerializedName("created_date")
    private Date createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != null ? !id.equals(review.id) : review.id != null) return false;
        if (taskId != null ? !taskId.equals(review.taskId) : review.taskId != null) return false;
        if (sender != null ? !sender.equals(review.sender) : review.sender != null) return false;
        if (senderImage != null ? !senderImage.equals(review.senderImage) : review.senderImage != null)
            return false;
        if (comment != null ? !comment.equals(review.comment) : review.comment != null)
            return false;
        if (rating != null ? !rating.equals(review.rating) : review.rating != null) return false;
        return createdDate != null ? createdDate.equals(review.createdDate) : review.createdDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (senderImage != null ? senderImage.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    public static class ReviewComparator implements Comparator<Review>{
        @Override
        public int compare(Review review, Review t1) {
            if(review.createdDate!=null && t1.getCreatedDate()!=null)
            return -1*review.getCreatedDate().compareTo(t1.createdDate);
            else return 0;
        }
    }

}
