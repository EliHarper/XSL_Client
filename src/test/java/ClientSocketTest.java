import msg.AuditMessage;
import org.junit.Assert;
import org.junit.Test;

import client.ClientSocket;

public class ClientSocketTest {
    @Test
    public void TestUnmarshal() {
        String marshalledMsgStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><auditMessage xsi:noNamespaceSchemaLocation=\"resources/AuditMessage.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><contents>U3dpZ2dpdHkgc3dvb3R5</contents></auditMessage>";
        String correctAnswer = "U3dpZ2dpdHkgc3dvb3R5";

        AuditMessage msg = ClientSocket.unmarshal(marshalledMsgStr);

        Assert.assertTrue(msg.getContents().equals(correctAnswer));
    }

    @Test
    public void TestDecode() {
        String encodedMsg = "U3dpZ2dpdHkgc3dvb3R5";
        AuditMessage testMsg = new AuditMessage(encodedMsg);
        String providedAnswer = ClientSocket.decode(testMsg);
        String correctAnswer = "Swiggity swooty";

        Assert.assertTrue(providedAnswer.equals(correctAnswer));
    }

    @Test
    public void TestIsXMLLike() {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><auditMessage xsi:noNamespaceSchemaLocation=\"resources/AuditMessage.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><contents>U3dpZ2dpdHkgc3dvb3R5</contents></auditMessage>";

        Assert.assertTrue(ClientSocket.isXMLLike(xmlStr));
    }
}
