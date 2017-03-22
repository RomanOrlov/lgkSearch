package lgk.nsbc.util;

import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.ui.TextField;

public class DoubleTextField extends TextField {
    public DoubleTextField(String caption) {
        super(caption);
        setHeight("40px");
        setWidth("100%");
        setNullRepresentation("");
        addValidator(new DoubleRangeValidator("Некорректное число", 0.d, Double.MAX_VALUE));
        setConverter(new StringToDoubleConverter());
    }
}
