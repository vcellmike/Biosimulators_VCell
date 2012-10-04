package cbit.vcell.message;


public final class VCQueueConsumer extends VCMessagingConsumer {
	
	public interface QueueListener {
		void onQueueMessage(VCMessage vcMessage, VCMessageSession session) throws RollbackException;
	}	
	
	private QueueListener listener = null;
	
	public VCQueueConsumer(VCellQueue queue, QueueListener listener, VCMessageSelector selector, String threadName){
		super(queue, selector, threadName);
		this.listener = listener;
	}
	public VCellQueue getQueue() {
		return (VCellQueue)getVCDestination();
	}
	public QueueListener getQueueListener() {
		return listener;
	}
}