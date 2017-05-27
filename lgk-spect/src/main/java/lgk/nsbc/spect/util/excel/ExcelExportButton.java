package lgk.nsbc.spect.util.excel;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ExcelExportButton extends HorizontalLayout {
    private Button button;

    public ExcelExportButton(String caption) {
        button = new Button(caption);
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(this);
        button.addClickListener(event -> fileDownloader.setFileDownloadResource(createResource()));
        addComponent(button);
    }

    protected abstract void exportToExcel(OutputStream outputStream) throws IOException;

    private StreamResource createResource() {
        return new StreamResource((StreamResource.StreamSource) () -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1_000_000);
            try {
                exportToExcel(byteArrayOutputStream);
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                Notification.show("Невозможно загрузить excel file");
                e.printStackTrace();
                return null;
            }
        }, "export.xlsx");
    }
}
