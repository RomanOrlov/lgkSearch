package lgk.nsbc.backend.search;

public interface SelectableInfo {

    boolean isSelected();

    void setSelected(boolean isSelected);

    String getRusName();

    void setN(long n);

    long getN();

    /**
     * У каждого критерия, или отображаемой информации есть такой уникальный номер.
     * @return Уникальный номер, соответствующий номеру в SelectColumnManager
     */
    int getUniqueNum();
}
