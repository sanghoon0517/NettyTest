package kr.co.jsh.test.echo.client.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.simple.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import kr.co.jsh.test.SimpleJSONObj;

@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	String className = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
	
	//서버와 연결 성공시 호출되는 메서드
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Connect() : [서버와 연결 성공]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
		
		JSONObject json = new SimpleJSONObj().getTestData();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(json);
		
		byte[] bs = bos.toByteArray();
		
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes(bs);
		System.out.println("SEND할 데이터 ["+buf.toString()+"]");
		
		ChannelFuture cf = ctx.writeAndFlush(buf);
		cf.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if(future.isSuccess()) {
					System.out.println("클라이언트에서 전송성공");
				} else {
					System.out.println("클라이언트에서 전송실패");
				}
			}
		});
	}
	
	//서버와 연결 실패시 호출되는 메서드
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Fail to Connect() : [서버와 연결 실패]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
	}
	
	//ChannelHandler가 ChannelPipeline에 추가될 때 호출되는 메서드
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to pipeline : ["+this.className+"클래스를 ChannelPipeline에 추가]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
	}
	
	//Channel이 EventLoop에 등록되고 입출력을 처리할 수 있으면 호출되는 메서드
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"클래스를 EventLoop에 추가] 입출력 처리 가능", CharsetUtil.UTF_8));
//		super.channelRegistered(ctx);
	}
	
	//Channel이 EventLoop에 해제되고 입출력을 처리할 수 없으면 호출되는 메서드
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"클래스를 EventLoop에 해제] 입출력 처리 불가능", CharsetUtil.UTF_8));
//		super.channelUnregistered(ctx);
	}
	
	//서버로부터 메시지를 수신하면 호출된다.
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] read = msg.array();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(read));
		
		JSONObject json = (JSONObject) ois.readObject();
		System.out.println("클라이언트가 받은 데이터 ["+json.toString()+"]");
		System.out.println("JSON PRETTY ["+new SimpleJSONObj().prettyJson(json)+"]");
		
	}
	
	//ChannelPipleline에서 처리 중에 오류가 발생시 호출
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace(); //에러 내용 print
		ctx.close(); //에러 발생시 닫기
	}
	
	//Channel에서 읽기 작업이 완료시 호출
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("읽기 작업이 완료됨 : ["+this.className+"]");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //대기중인 메시지를 플러시하고 채널을 닫음
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	
}
