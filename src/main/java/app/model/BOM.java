package app.model;

/**
 * POJO representing a BOM (Bill of Materials) entry in the application.
 */
public class BOM {
    private int id;
    private int offerId;
    private int materialId;
    private int quantity;
    private String unit;

    /**
     * Default constructor.
     */
    public BOM() {
    }

    /**
     * Full-args constructor.
     *
     * @param id         the BOM entry ID
     * @param offerId    the associated offer ID
     * @param materialId the associated material ID
     * @param quantity   the quantity of material
     * @param unit       the unit of measure
     */
    public BOM(int id, int offerId, int materialId, int quantity, String unit) {
        this.id = id;
        this.offerId = offerId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "BOM{" +
                "id=" + id +
                ", offerId=" + offerId +
                ", materialId=" + materialId +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}

