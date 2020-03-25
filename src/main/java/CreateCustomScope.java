import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.util.Map;

public class CreateCustomScope implements ActionListener {

    public void processAction(final ActionEvent event) throws AbortProcessingException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();

        CustomScope customScope = new CustomScope();
        sessionMap.put(CustomScope.SCOPE_NAME, customScope);

        customScope.notifyCreate(facesContext);

    }

}