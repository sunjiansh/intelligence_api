import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_device_command.*;
import com.ctg.ag.sdk.core.constant.Scheme;
import com.ctg.ag.sdk.core.model.ApiCallBack;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


public class AepDeviceCommandDemo {

	// 没有签名同步调用接口示例
	@Test
	public void testApi() throws Exception {

		AepDeviceCommandClient client = AepDeviceCommandClient.newClient().build();

		{
			CreateCommandRequest request = new CreateCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CreateCommand(request));
		}
		
		{
			QueryCommandListRequest request = new QueryCommandListRequest();
			// request.setParam..  	// set your request params here
			request.setParamMasterKey("2e0c19abd7414b8e95b96928d68c9cf8");
			request.setParamProductId(15518212);
			request.setParamDeviceId("8a940782d52d493581770cc64cdfcba2");
			QueryCommandListResponse response = client.QueryCommandList(request);

			System.out.println(response);
		}
		
		{
			QueryCommandRequest request = new QueryCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryCommand(request));
		}
		
		{
			CancelCommandRequest request = new CancelCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CancelCommand(request));
		}
		
		client.shutdown();

	}

	// 没有签名异步调用接口示例
	@Test
	public void testApiAsync() throws Exception {

		AepDeviceCommandClient client = AepDeviceCommandClient.newClient().build();

		{
			
			List<Future<CreateCommandResponse>> res = new ArrayList<Future<CreateCommandResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				CreateCommandRequest request = new CreateCommandRequest();
				// request.setParam..  	// set your request params here

				res.add(client.CreateCommand(request, new ApiCallBack<CreateCommandRequest, CreateCommandResponse>() {
					@Override
					public void onFailure(CreateCommandRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(CreateCommandRequest request, CreateCommandResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<CreateCommandResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QueryCommandListResponse>> res = new ArrayList<Future<QueryCommandListResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryCommandListRequest request = new QueryCommandListRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryCommandList(request, new ApiCallBack<QueryCommandListRequest, QueryCommandListResponse>() {
					@Override
					public void onFailure(QueryCommandListRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryCommandListRequest request, QueryCommandListResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryCommandListResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QueryCommandResponse>> res = new ArrayList<Future<QueryCommandResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryCommandRequest request = new QueryCommandRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryCommand(request, new ApiCallBack<QueryCommandRequest, QueryCommandResponse>() {
					@Override
					public void onFailure(QueryCommandRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryCommandRequest request, QueryCommandResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryCommandResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<CancelCommandResponse>> res = new ArrayList<Future<CancelCommandResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				CancelCommandRequest request = new CancelCommandRequest();
				// request.setParam..  	// set your request params here

				res.add(client.CancelCommand(request, new ApiCallBack<CancelCommandRequest, CancelCommandResponse>() {
					@Override
					public void onFailure(CancelCommandRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(CancelCommandRequest request, CancelCommandResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<CancelCommandResponse> future : res)
				System.out.println(future.get());

		}
		
		client.shutdown();
	}

	// 没有签名https同步调用接口示例
	@Test
	public void testApiWithSsl() throws Exception {

		AepDeviceCommandClient client = AepDeviceCommandClient.newClient().scheme(Scheme.HTTPS).build();

		{
			CreateCommandRequest request = new CreateCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CreateCommand(request));
		}
		
		{
			QueryCommandListRequest request = new QueryCommandListRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryCommandList(request));
		}
		
		{
			QueryCommandRequest request = new QueryCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryCommand(request));
		}
		
		{
			CancelCommandRequest request = new CancelCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CancelCommand(request));
		}
		
		client.shutdown();
	}

	// 签名同步调用接口示例
	@Test
	public void testApiWithSignature() throws Exception {

		AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey("dyg2BzXqigf").appSecret("nl8mvgEFeu").build();

		{
			CreateCommandRequest request = new CreateCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CreateCommand(request));
		}
		
		{
			QueryCommandListRequest request = new QueryCommandListRequest();
			// request.setParam..  	// set your request params here
			request.setParamMasterKey("2e0c19abd7414b8e95b96928d68c9cf8");
			request.setParamProductId(15518212);
			request.setParamDeviceId("8a940782d52d493581770cc64cdfcba2");

			QueryCommandListResponse response = client.QueryCommandList(request);

			System.out.println(new String(response.getBody()));
		}
		
		{
			QueryCommandRequest request = new QueryCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryCommand(request));
		}
		
		{
			CancelCommandRequest request = new CancelCommandRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.CancelCommand(request));
		}
		
		client.shutdown();
	}

}
