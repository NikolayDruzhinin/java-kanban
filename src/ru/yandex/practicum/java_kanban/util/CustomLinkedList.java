package ru.yandex.practicum.java_kanban.util;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList<T extends Task> {

    private Node head = null;
    private Node tail = null;
    private long size;
    private Map<Long, Node> ids = new HashMap<>();

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public long getSize() {
        return size;
    }

    public Map<Long, Node> getIds() {
        return new HashMap<>(ids);
    }

    public void linkLast(T task) {
        if (ids.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node node;
        if (head == null) {
            node = new Node(null, task, null);
            head = node;
        } else {
            node = new Node(tail, task, null);
            tail.next = node;
        }
        tail = node;
        ids.put(task.getId(), node);
        ++size;
    }

    private void replaceNode(T task) {
        Node nodeToReplace = ids.get(task.getId());
        Node newNode = new Node(nodeToReplace.prev, task, nodeToReplace.next);
        if (nodeToReplace.equals(head)) {
            if (size > 1) {
                nodeToReplace.next.prev = newNode;
            }
            head = newNode;
        } else if (nodeToReplace.equals(tail)) {
            if (size > 1) {
                nodeToReplace.prev.next = newNode;
            }
            tail = newNode;
        } else {
            nodeToReplace.next.prev = newNode;
            nodeToReplace.prev.next = newNode;
        }
        nodeToReplace.next = null;
        nodeToReplace.prev = null;
        ids.put(task.getId(), newNode);
    }

    public List<T> getTasks() {
        List<T> result = new ArrayList<>();
        if (head != null) {
            Node<T> tmp = head;
            do {
                result.add(tmp.task);
                tmp = tmp.next;
            } while (tmp != null);
        }
        return result;
    }

    public void remove(Long id) {
        if (ids.containsKey(id)) {
            Node nodeToRemove = ids.get(id);
            if (nodeToRemove.equals(head)) {
                if (size > 1) {
                    head = nodeToRemove.next;
                    nodeToRemove.next.prev = null;
                } else {
                    head = null;
                    tail = null;
                }
            } else if (nodeToRemove.equals(tail)) {
                tail = nodeToRemove.prev;
                nodeToRemove.prev.next = null;
            } else {
                nodeToRemove.prev.next = nodeToRemove.next;
                nodeToRemove.next.prev = nodeToRemove.prev;
            }
            ids.remove(id);
            --size;
        }
    }

    public T get(Long id) {
        Node<T> result = ids.get(id);
        return result.task;
    }
}
