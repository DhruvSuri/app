package com.app.azazte.azazte.Event;

import com.app.azazte.azazte.Beans.Bubble;
import com.app.azazte.azazte.Beans.BubbleWrapper;

import java.util.List;

/**
 * Created by home on 26/08/16.
 */
public class BubbleEvent {
    private List<Bubble> bubbleList;

    public BubbleEvent(List<Bubble> bubbleList) {
        this.bubbleList = bubbleList;
    }

    public List<Bubble> getBubbleList() {
        return bubbleList;
    }

    public void setBubbleList(List<Bubble> bubbleList) {
        this.bubbleList = bubbleList;
    }
}
