import React, { useState, useRef } from 'react';
import toast from 'react-hot-toast';
import { sendEmailWithFile } from './services/email.service';
import { Editor } from '@tinymce/tinymce-react';

function EmailSender() {
  const [emailData, setEmailData] = useState({
    to: '',
    subject: '',
    message: '',
  });
  const [selectedFile, setSelectedFile] = useState(null); // For file attachment
  const [isLoading, setIsLoading] = useState(false);
  const editorRef = useRef(null);

  // Handle input changes
  const handleFieldChange = (event, name) => {
    setEmailData({ ...emailData, [name]: event.target.value });
  };

  // Handle file input
  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  // Handle form submission
  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validate fields
    if (!emailData.to.trim() || !emailData.subject.trim() || !emailData.message.trim()) {
      toast.error('All fields are required!');
      return;
    }

    if (!selectedFile) {
      toast.error('Please attach a file!');
      return;
    }

    setIsLoading(true); // Show loader

    try {
      const formData = new FormData();
      formData.append('emailRequest', new Blob([JSON.stringify(emailData)], { type: 'application/json' }));
      formData.append('multipartFile', selectedFile);

      await sendEmailWithFile(formData);
      toast.success('Email sent successfully with attachment!');
      setEmailData({
        to: '',
        subject: '',
        message: '',
      });
      setSelectedFile(null); // Clear file
      if (editorRef.current) editorRef.current.setContent(''); // Clear editor content
    } catch (error) {
      toast.error('An error occurred while sending the email.');
    } finally {
      setIsLoading(false); // Hide loader
    }
  };

  const handleClear = () => {
    setEmailData({
      to: '',
      subject: '',
      message: '',
    });
    setSelectedFile(null); // Clear file
    if (editorRef.current) editorRef.current.setContent(''); // Clear editor content
  };

  return (
    <div className="w-full min-h-screen flex justify-center items-center bg-gray-100 py-12">
      <div className="email_card w-full max-w-2xl rounded-lg border shadow-lg p-8 bg-white">
        <h1 className="text-gray-900 text-2xl font-semibold mb-6 text-center">Email Sender</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="to-input" className="block mb-2 text-sm font-medium text-gray-900">
              To
            </label>
            <input
              type="text"
              id="to-input"
              name="to"
              value={emailData.to}
              onChange={(e) => handleFieldChange(e, 'to')}
              className="block w-full p-3 text-gray-900 border border-gray-300 rounded-md bg-gray-50 text-sm focus:ring-blue-500 focus:border-blue-500"
              placeholder="Enter email address here"
            />
          </div>

          <div className="mb-4">
            <label htmlFor="subject-input" className="block mb-2 text-sm font-medium text-gray-900">
              Subject
            </label>
            <input
              type="text"
              id="subject-input"
              name="subject"
              value={emailData.subject}
              onChange={(e) => handleFieldChange(e, 'subject')}
              className="block w-full p-3 text-gray-900 border border-gray-300 rounded-md bg-gray-50 text-sm focus:ring-blue-500 focus:border-blue-500"
              placeholder="Enter email subject here"
            />
          </div>

          <div className="mb-4">
            <label htmlFor="message-input" className="block mb-2 text-sm font-medium text-gray-900">
              Message
            </label>
            <Editor
              onEditorChange={() =>
                setEmailData({ ...emailData, message: editorRef.current.getContent() })
              }
              onInit={(_evt, editor) => (editorRef.current = editor)}
              apiKey="34ftac1z3a3qxv6qgpwvg2jievrmtiffdh6npcc6jqld175l"
              initialValue="<p>Type your message here...</p>"
              init={{
                plugins: ['link', 'lists', 'media', 'table', 'wordcount'],
                toolbar: 'undo redo | bold italic | bullist numlist | link',
              }}
            />
          </div>

          <div className="mb-4">
            <label htmlFor="file-input" className="block mb-2 text-sm font-medium text-gray-900">
              Attach File
            </label>
            <input
              type="file"
              id="file-input"
              onChange={handleFileChange}
              className="block w-full p-2 text-gray-900 border border-gray-300 rounded-md bg-gray-50 text-sm focus:ring-blue-500 focus:border-blue-500"
            />
          </div>

          {/* Loader */}
          {isLoading && (
            <div className="flex justify-center mb-4">
              <svg
                aria-hidden="true"
                className="w-8 h-8 text-gray-200 animate-spin fill-blue-600"
                viewBox="0 0 100 101"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M100 50.59c0 27.61-22.39 50-50 50S0 78.2 0 50.59 22.39 0.59 50 0.59s50 22.39 50 50z"
                  fill="currentColor"
                />
                <path
                  d="M93.97 39.04c2.43-.64 3.9-3.13 3.05-5.49a49.33 49.33 0 0 0-8.15-12.21c-4.97-6.18-11.45-11.05-18.33-14.29a48.7 48.7 0 0 0-14.94-3.8c-5-1.1-10.08-1.02-15.04.17-2.48.44-4 2.94-3.36 5.36.64 2.42 3.13 3.89 5.49 3.04a40.6 40.6 0 0 1 29.8 7.9c4.7 3.21 8.78 7.18 12.12 11.64a40.6 40.6 0 0 1 5.82 13.21c.9 2.3 3.36 3.76 5.78 3.12z"
                  fill="currentFill"
                />
              </svg>
              <p className="mt-2 text-blue-600 text-sm font-medium">Email is sending...</p>
            </div>
          )}

          <div className="flex justify-between mt-6">
            <button
              type="submit"
              className="w-1/2 mr-2 p-3 bg-blue-500 text-white rounded-md text-sm font-medium hover:bg-blue-600 transition duration-200"
              disabled={isLoading}
            >
              {isLoading ? 'Sending...' : 'Send Email'}
            </button>

            <button
              type="button"
              onClick={handleClear}
              className="w-1/2 ml-2 p-3 bg-gray-500 text-white rounded-md text-sm font-medium hover:bg-gray-600 transition duration-200"
              disabled={isLoading}
            >
              Clear
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EmailSender;
