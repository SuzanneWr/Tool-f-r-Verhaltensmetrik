package com.mygdx.bisimulationgame.Util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.bisimulationgame.datatypes.LabelledMarkovChain;

public class SaveUtil {

    private FileHandle file;
    private Json json;

    public SaveUtil(){
        json = new Json();
    }

    public void saveLabelledMarkovChain(LabelledMarkovChain markovChain, String fileName){
        file = Gdx.files.local("markovchains/" + fileName);

        String toSave = json.prettyPrint(markovChain);
        file.writeString(toSave, false);
    }

    public LabelledMarkovChain loadLabelledMarkovChain(String fileName){
        file = Gdx.files.local("markovchains/" + fileName);

        LabelledMarkovChain markovChain;
        String toLoad = file.readString();
        markovChain = json.fromJson(LabelledMarkovChain.class, toLoad);

        return markovChain;
    }

    public Array<String> loadFileNames(){
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("markovchains/").list();
        for(FileHandle file : files){
            fileNames.add(file.name());
        }
        return  fileNames;
    }

    public boolean deleteFile(String fileName){
        if(Gdx.files.local("markovchains/" + fileName).exists()){
            Gdx.files.local("markovchains/" + fileName).delete();
            return true;
        }
        return false;
    }
}
