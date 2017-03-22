package lgk.nsbc.util;

import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.ui.TextField;

public class LongTextField extends TextField {
    public LongTextField(String caption) {
        super(caption);
        setHeight("40px");
        setWidth("100%");
        setNullRepresentation("");
        addValidator(new LongRangeValidator("Некорректное число", 0L, Long.MAX_VALUE));
        setConverter(new StringToLongConverter());
    }
}
