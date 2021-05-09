package extended.ui.leisurelodgehotelbooking;

public class ModelHotel {
    private String hotelId, hotelTitle, hotelDescription, hotelCategory, hotelQuantity, hotelIcon,
            originalPrice, discountPrice, discountNote, discountAvailable, timestamp , uid;

    public ModelHotel() {
    }

    public ModelHotel(String hotelId, String hotelTitle, String hotelDescription, String hotelCategory,
                      String hotelQuantity, String hotelIcon, String originalPrice, String discountPrice, String discountNote,
                      String discountAvailable, String timestamp, String uid) {
        this.hotelId = hotelId;
        this.hotelTitle = hotelTitle;
        this.hotelDescription = hotelDescription;
        this.hotelCategory = hotelCategory;
        this.hotelQuantity = hotelQuantity;
        this.hotelIcon = hotelIcon;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountNote = discountNote;
        this.discountAvailable = discountAvailable;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelTitle() {
        return hotelTitle;
    }

    public void setHotelTitle(String hotelTitle) {
        this.hotelTitle = hotelTitle;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelCategory() {
        return hotelCategory;
    }

    public void setHotelCategory(String hotelCategory) {
        this.hotelCategory = hotelCategory;
    }

    public String getHotelQuantity() {
        return hotelQuantity;
    }

    public void setHotelQuantity(String hotelQuantity) {
        this.hotelQuantity = hotelQuantity;
    }

    public String getHotelIcon() {
        return hotelIcon;
    }

    public void setHotelIcon(String hotelIcon) {
        this.hotelIcon = hotelIcon;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountNote() {
        return discountNote;
    }

    public void setDiscountNote(String discountNote) {
        this.discountNote = discountNote;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
