package bakalauras.demo.web.domain;

public class SmartIdInitReq {

    String relyingPartyUUID;
    String relyingPartyName;
    String hash;
    String hashType;

    public SmartIdInitReq() {};

    public SmartIdInitReq(String relyingPartyUUID, String relyingPartyName, String hash, String hashType) {
        this.relyingPartyUUID = relyingPartyUUID;
        this.relyingPartyName = relyingPartyName;
        this.hash = hash;
        this.hashType = hashType;
    }

    public String getRelyingPartyUUID() {
        return relyingPartyUUID;
    }

    public String getRelyingPartyName() {
        return relyingPartyName;
    }

    public String getHash() {
        return hash;
    }

    public String getHashType() {
        return hashType;
    }

}
