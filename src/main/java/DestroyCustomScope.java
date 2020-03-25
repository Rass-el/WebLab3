import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.util.Map;

public class DestroyCustomScope implements ActionListener {

    public void processAction(final ActionEvent event) throws AbortProcessingException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();

        CustomScope customScope = (CustomScope) sessionMap.get(CustomScope.SCOPE_NAME);
        customScope.notifyDestroy(facesContext);

        sessionMap.remove(CustomScope.SCOPE_NAME);

    }

}