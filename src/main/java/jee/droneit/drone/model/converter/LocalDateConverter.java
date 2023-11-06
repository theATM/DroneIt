package jee.droneit.drone.model.converter;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@FacesConverter(value = "LocalDateConverter")
public class LocalDateConverter implements Converter<LocalDate>
{
    @Override
    public LocalDate getAsObject(FacesContext facesContext, UIComponent uiComponent, String str)
    {
        if(!str.isEmpty())
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try
            {
                return LocalDate.parse(str, dtf);
            }
            catch (DateTimeException e)
            {
                FacesMessage msg = new FacesMessage("LocalDate parse error.", "Invalid date format");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ConverterException(msg);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, LocalDate date)
    {
        return date == null ? "" : date.toString();
    }
}
