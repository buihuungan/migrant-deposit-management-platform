function deepClone(obj) {
    return JSON.parse(JSON.stringify(obj));
}
function shallowClone(obj) {
    let shallow = {};
    Object.assign(shallow, obj);
    return shallow;
}
export { deepClone, shallowClone }