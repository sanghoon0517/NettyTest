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
	
	//������ ���� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Connect() : [������ ���� ����]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
		
		JSONObject json = new SimpleJSONObj().getTestData();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(json);
		
		byte[] bs = bos.toByteArray();
		
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes(bs);
		System.out.println("SEND�� ������ ["+buf.toString()+"]");
		
		ChannelFuture cf = ctx.writeAndFlush(buf);
		cf.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if(future.isSuccess()) {
					System.out.println("Ŭ���̾�Ʈ���� ���ۼ���");
				} else {
					System.out.println("Ŭ���̾�Ʈ���� ���۽���");
				}
			}
		});
	}
	
	//������ ���� ���н� ȣ��Ǵ� �޼���
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Fail to Connect() : [������ ���� ����]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	//ChannelHandler�� ChannelPipeline�� �߰��� �� ȣ��Ǵ� �޼���
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to pipeline : ["+this.className+"Ŭ������ ChannelPipeline�� �߰�]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	//Channel�� EventLoop�� ��ϵǰ� ������� ó���� �� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"Ŭ������ EventLoop�� �߰�] ����� ó�� ����", CharsetUtil.UTF_8));
//		super.channelRegistered(ctx);
	}
	
	//Channel�� EventLoop�� �����ǰ� ������� ó���� �� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"Ŭ������ EventLoop�� ����] ����� ó�� �Ұ���", CharsetUtil.UTF_8));
//		super.channelUnregistered(ctx);
	}
	
	//�����κ��� �޽����� �����ϸ� ȣ��ȴ�.
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] read = msg.array();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(read));
		
		JSONObject json = (JSONObject) ois.readObject();
		System.out.println("Ŭ���̾�Ʈ�� ���� ������ ["+json.toString()+"]");
		System.out.println("JSON PRETTY ["+new SimpleJSONObj().prettyJson(json)+"]");
		
	}
	
	//ChannelPipleline���� ó�� �߿� ������ �߻��� ȣ��
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace(); //���� ���� print
		ctx.close(); //���� �߻��� �ݱ�
	}
	
	//Channel���� �б� �۾��� �Ϸ�� ȣ��
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("�б� �۾��� �Ϸ�� : ["+this.className+"]");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //������� �޽����� �÷����ϰ� ä���� ����
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	
}
