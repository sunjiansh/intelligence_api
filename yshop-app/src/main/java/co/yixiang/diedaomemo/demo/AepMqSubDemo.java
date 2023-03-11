

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Future;

import org.junit.Test;

import com.ctg.ag.sdk.core.constant.Scheme;
import com.ctg.ag.sdk.core.model.ApiCallBack;

import com.ctg.ag.sdk.biz.AepMqSubClient;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryServiceStateRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryServiceStateResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.OpenMqServiceRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.OpenMqServiceResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicInfoRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicInfoResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicCacheInfoRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicCacheInfoResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicsRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.QueryTopicsResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.QuerySubRulesRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.QuerySubRulesResponse;
import com.ctg.ag.sdk.biz.aep_mq_sub.ClosePushServiceRequest;
import com.ctg.ag.sdk.biz.aep_mq_sub.ClosePushServiceResponse;


public class AepMqSubDemo {

	// 没有签名同步调用接口示例
	@Test
	public void testApi() throws Exception {

		AepMqSubClient client = AepMqSubClient.newClient().build();

		{
			QueryServiceStateRequest request = new QueryServiceStateRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryServiceState(request));
		}
		
		{
			OpenMqServiceRequest request = new OpenMqServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.OpenMqService(request));
		}
		
		{
			QueryTopicInfoRequest request = new QueryTopicInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicInfo(request));
		}
		
		{
			QueryTopicCacheInfoRequest request = new QueryTopicCacheInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicCacheInfo(request));
		}
		
		{
			QueryTopicsRequest request = new QueryTopicsRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopics(request));
		}
		
		{
			QuerySubRulesRequest request = new QuerySubRulesRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QuerySubRules(request));
		}
		
		{
			ClosePushServiceRequest request = new ClosePushServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.ClosePushService(request));
		}
		
		client.shutdown();

	}

	// 没有签名异步调用接口示例
	@Test
	public void testApiAsync() throws Exception {

		AepMqSubClient client = AepMqSubClient.newClient().build();

		{
			
			List<Future<QueryServiceStateResponse>> res = new ArrayList<Future<QueryServiceStateResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryServiceStateRequest request = new QueryServiceStateRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryServiceState(request, new ApiCallBack<QueryServiceStateRequest, QueryServiceStateResponse>() {
					@Override
					public void onFailure(QueryServiceStateRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryServiceStateRequest request, QueryServiceStateResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryServiceStateResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<OpenMqServiceResponse>> res = new ArrayList<Future<OpenMqServiceResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				OpenMqServiceRequest request = new OpenMqServiceRequest();
				// request.setParam..  	// set your request params here

				res.add(client.OpenMqService(request, new ApiCallBack<OpenMqServiceRequest, OpenMqServiceResponse>() {
					@Override
					public void onFailure(OpenMqServiceRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(OpenMqServiceRequest request, OpenMqServiceResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<OpenMqServiceResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QueryTopicInfoResponse>> res = new ArrayList<Future<QueryTopicInfoResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryTopicInfoRequest request = new QueryTopicInfoRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryTopicInfo(request, new ApiCallBack<QueryTopicInfoRequest, QueryTopicInfoResponse>() {
					@Override
					public void onFailure(QueryTopicInfoRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryTopicInfoRequest request, QueryTopicInfoResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryTopicInfoResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QueryTopicCacheInfoResponse>> res = new ArrayList<Future<QueryTopicCacheInfoResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryTopicCacheInfoRequest request = new QueryTopicCacheInfoRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryTopicCacheInfo(request, new ApiCallBack<QueryTopicCacheInfoRequest, QueryTopicCacheInfoResponse>() {
					@Override
					public void onFailure(QueryTopicCacheInfoRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryTopicCacheInfoRequest request, QueryTopicCacheInfoResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryTopicCacheInfoResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QueryTopicsResponse>> res = new ArrayList<Future<QueryTopicsResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QueryTopicsRequest request = new QueryTopicsRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QueryTopics(request, new ApiCallBack<QueryTopicsRequest, QueryTopicsResponse>() {
					@Override
					public void onFailure(QueryTopicsRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QueryTopicsRequest request, QueryTopicsResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QueryTopicsResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<QuerySubRulesResponse>> res = new ArrayList<Future<QuerySubRulesResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				QuerySubRulesRequest request = new QuerySubRulesRequest();
				// request.setParam..  	// set your request params here

				res.add(client.QuerySubRules(request, new ApiCallBack<QuerySubRulesRequest, QuerySubRulesResponse>() {
					@Override
					public void onFailure(QuerySubRulesRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(QuerySubRulesRequest request, QuerySubRulesResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<QuerySubRulesResponse> future : res)
				System.out.println(future.get());

		}
		
		{
			
			List<Future<ClosePushServiceResponse>> res = new ArrayList<Future<ClosePushServiceResponse>>();
			
			// multi request
			for (int i = 0; i < 5; i++) {
			
				ClosePushServiceRequest request = new ClosePushServiceRequest();
				// request.setParam..  	// set your request params here

				res.add(client.ClosePushService(request, new ApiCallBack<ClosePushServiceRequest, ClosePushServiceResponse>() {
					@Override
					public void onFailure(ClosePushServiceRequest request, Exception e) {
						e.printStackTrace();
					}
		
					@Override
					public void onResponse(ClosePushServiceRequest request, ClosePushServiceResponse response) {
						System.out.println("Receive data and handle it");
					}
				}));
			
			}
			
			// wait and collect all data
			for (Future<ClosePushServiceResponse> future : res)
				System.out.println(future.get());

		}
		
		client.shutdown();
	}

	// 没有签名https同步调用接口示例
	@Test
	public void testApiWithSsl() throws Exception {

		AepMqSubClient client = AepMqSubClient.newClient().scheme(Scheme.HTTPS).build();

		{
			QueryServiceStateRequest request = new QueryServiceStateRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryServiceState(request));
		}
		
		{
			OpenMqServiceRequest request = new OpenMqServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.OpenMqService(request));
		}
		
		{
			QueryTopicInfoRequest request = new QueryTopicInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicInfo(request));
		}
		
		{
			QueryTopicCacheInfoRequest request = new QueryTopicCacheInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicCacheInfo(request));
		}
		
		{
			QueryTopicsRequest request = new QueryTopicsRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopics(request));
		}
		
		{
			QuerySubRulesRequest request = new QuerySubRulesRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QuerySubRules(request));
		}
		
		{
			ClosePushServiceRequest request = new ClosePushServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.ClosePushService(request));
		}
		
		client.shutdown();
	}

	// 签名同步调用接口示例
	@Test
	public void testApiWithSignature() throws Exception {

		AepMqSubClient client = AepMqSubClient.newClient().appKey("Your app key here").appSecret("Your app secret here").build();

		{
			QueryServiceStateRequest request = new QueryServiceStateRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryServiceState(request));
		}
		
		{
			OpenMqServiceRequest request = new OpenMqServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.OpenMqService(request));
		}
		
		{
			QueryTopicInfoRequest request = new QueryTopicInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicInfo(request));
		}
		
		{
			QueryTopicCacheInfoRequest request = new QueryTopicCacheInfoRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopicCacheInfo(request));
		}
		
		{
			QueryTopicsRequest request = new QueryTopicsRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QueryTopics(request));
		}
		
		{
			QuerySubRulesRequest request = new QuerySubRulesRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.QuerySubRules(request));
		}
		
		{
			ClosePushServiceRequest request = new ClosePushServiceRequest();
			// request.setParam..  	// set your request params here
			System.out.println(client.ClosePushService(request));
		}
		
		client.shutdown();
	}

}
