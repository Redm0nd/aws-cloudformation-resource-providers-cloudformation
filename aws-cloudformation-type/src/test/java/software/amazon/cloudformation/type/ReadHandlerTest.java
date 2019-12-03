package software.amazon.cloudformation.type;

import org.mockito.ArgumentMatchers;
import software.amazon.awssdk.services.cloudformation.model.DescribeTypeResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ReadHandlerTest {

    ReadHandler handler;

    @Mock
    private AmazonWebServicesClientProxy proxy;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        handler = new ReadHandler();
    }

    @Test
    public void handleRequest_SimpleSuccess() {
        final DescribeTypeResponse describeTypeResponse = DescribeTypeResponse.builder()
            .typeName("AWS::Demo::Resource")
            .type("RESOURCE")
            .visibility("PRIVATE")
            .sourceUrl("https://github.com/myorg/resource/repo.git")
            .deprecatedStatus("LIVE")
            .build();

        doReturn(describeTypeResponse)
            .when(proxy)
            .injectCredentialsAndInvokeV2(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
            );

        final ResourceModel inModel = ResourceModel.builder()
            .typeName("AWS::Demo::Resource")
            .type("RESOURCE")
            .build();

        final ResourceModel outModel = ResourceModel.builder()
            .typeName("AWS::Demo::Resource")
            .type("RESOURCE")
            .visibility("PRIVATE")
            .sourceUrl("https://github.com/myorg/resource/repo.git")
            .deprecatedStatus("LIVE")
            .build();


        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
            .desiredResourceState(inModel)
            .build();

        final ProgressEvent<ResourceModel, CallbackContext> response
            = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(outModel);
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }
}
