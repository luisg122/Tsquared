package com.example.tsquared.DiffUtilsItems;

import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class AnswersDiffCallback extends DiffUtil.Callback {

    private final List<Object> oldAnswerList;
    private final List<Object> newAnswerList;

    public AnswersDiffCallback(List<Object> oldAnswerList, List<Object> newAnswerList) {
        this.oldAnswerList = oldAnswerList;
        this.newAnswerList = newAnswerList;
    }

    @Override
    public int getOldListSize() {
        return oldAnswerList.size();
    }

    @Override
    public int getNewListSize() {
        return newAnswerList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldAnswerItem = oldAnswerList.get(oldItemPosition);
        Object newAnswerItem = newAnswerList.get(newItemPosition);

        if(oldAnswerItem instanceof AnswerModel && newAnswerItem instanceof AnswerModel){
            AnswerModel oldAnswer = (AnswerModel) oldAnswerItem;
            AnswerModel newAnswer = (AnswerModel) newAnswerItem;

            return oldAnswer.getName().equals(newAnswer.getName());
        }

        else if(oldAnswerItem instanceof AnswerWithImages && newAnswerItem instanceof AnswerWithImages){
            AnswerWithImages oldAnswer = (AnswerWithImages) oldAnswerItem;
            AnswerWithImages newAnswer = (AnswerWithImages) newAnswerItem;

            return oldAnswer.getName().equals(newAnswer.getName());
        }

        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldAnswerItem = oldAnswerList.get(oldItemPosition);
        Object newAnswerItem = newAnswerList.get(newItemPosition);

        if(oldAnswerItem instanceof AnswerModel && newAnswerItem instanceof AnswerModel){
            AnswerModel oldAnswer = (AnswerModel) oldAnswerItem;
            AnswerModel newAnswer = (AnswerModel) newAnswerItem;

            return oldAnswer.getAnswer().equals(newAnswer.getAnswer()) &&
                    oldAnswer.getName().equals(newAnswer.getName()) &&
                    oldAnswer.getDateAnswered().equals(newAnswer.getDateAnswered());
        }

        else if(oldAnswerItem instanceof AnswerWithImages && newAnswerItem instanceof AnswerWithImages){
            AnswerWithImages oldAnswer = (AnswerWithImages) oldAnswerItem;
            AnswerWithImages newAnswer = (AnswerWithImages) newAnswerItem;

            return oldAnswer.getAnswer().equals(newAnswer.getAnswer()) &&
                    oldAnswer.getName().equals(newAnswer.getName()) &&
                    oldAnswer.getDateAnswered().equals(newAnswer.getDateAnswered());
        }

        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}