package grails.plugin.cucumber.io

import cucumber.io.FlatteningIterator
import cucumber.io.Resource
import static cucumber.io.ClasspathIterable.hasSuffix

class FileResourceIterator implements Iterator<Resource> {
    private FlatteningIterator flatteningIterator

    FileResourceIterator (File root, File file, String suffix, FileFilter filter) {
        flatteningIterator = new FlatteningIterator (
            new cucumber.io.FileResourceIterator.FileIterator (
                root, file, createFilter (getDefaultFilter (suffix), filter)
        ))
    }

    @Override
    public boolean hasNext () {
        return flatteningIterator.hasNext ()
    }

    @Override
    public Resource next () {
        return (Resource) flatteningIterator.next ()
    }

    @Override
    public void remove () {
        throw new UnsupportedOperationException ()
    }

    private FileFilter getDefaultFilter (String suffix) {
        new FileFilter () {
            @Override
            public boolean accept (File file) {
                return file.isDirectory () || hasSuffix (suffix, file.getPath ())
            }
        }
    }

    private FileFilter createFilter (FileFilter... filters) {
        new FileFilter () {
            @Override
            boolean accept (File file) {
                filters.inject (true) { match, filter ->
                    match &= filter.accept (file)
                }
            }
        }
    }
}
